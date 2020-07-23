package com.bsam.chatbot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kendra.AWSkendra;
import com.amazonaws.services.kendra.AWSkendraClientBuilder;
import com.amazonaws.services.kendra.model.AdditionalResultAttribute;
import com.amazonaws.services.kendra.model.QueryRequest;
import com.amazonaws.services.kendra.model.QueryResult;
import com.amazonaws.services.kendra.model.QueryResultItem;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HandlerKendra implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final String DOCUMENT = "DOCUMENT";
	private static final String ANSWER = "ANSWER";
	private static final String QUESTION_ANSWER = "QUESTION_ANSWER";

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

		LambdaLogger logger = context.getLogger();
		Map<String, String> corsMap = new HashMap<>();
		corsMap.put("Access-Control-Allow-Origin", "*");

		String queryText = input.getQueryStringParameters().get("queryText");
		logger.log("******* Input Query: " + queryText);

		ResultJson resultJson = new ResultJson();
		ObjectMapper obj = new ObjectMapper();
		resultJson.setQuery(queryText);
		try {
			//        KendraClient kendra = KendraClient.builder().build();
			//			BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv().get("AWS_ACCESS_KEY_ID"), System.getenv().get("AWS_SECRET_KEY"));
			BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIASVX3YZPCS4UDZUGP", "ei8bbG3xvl6fxcHSr1sa0+/DQWEFSXUJgqtNhXFd");

			AWSkendra kendra = AWSkendraClientBuilder.standard().withRegion("us-east-1")
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();


			QueryRequest query = new QueryRequest();
			query.setIndexId("b44f2d70-0554-4cbf-871a-9a88bbe04c7c");
			query.setQueryText(queryText);


			QueryResult results = kendra.query(query);


			if(results.getResultItems().size() == 0){
				resultJson.setResultText("No Match Found");
				return new APIGatewayProxyResponseEvent()
						.withStatusCode(200)
						.withHeaders(corsMap)
						.withBody(obj.writeValueAsString(resultJson));
			}

			//Iterate through all results and return the one which matches from FAQ
			for(QueryResultItem type : results.getResultItems()) {
				if(QUESTION_ANSWER.equals(type.getType())) {
					String faqAnswerText = type.getDocumentExcerpt().getText();
					resultJson.setResultText(faqAnswerText);
					return new APIGatewayProxyResponseEvent()
							.withStatusCode(200)
							.withHeaders(corsMap)
							.withBody(cleanUptext(obj.writeValueAsString(resultJson)));
				}
			}

			//Iterate through all results and find match with topAnswer as true
			for(QueryResultItem type : results.getResultItems()) {
				if (!ANSWER.equals(type.getType())){
					continue;
				}

				List<AdditionalResultAttribute> addList =  type.getAdditionalAttributes();

				if(addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().size() > 0 &&
						addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().get(0).getTopAnswer()) {
					String faqAnswerText = addList.get(0).getValue().getTextWithHighlightsValue().getText();
					int begin = addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().get(0).getBeginOffset();
					int end = addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().get(0).getEndOffset();
					resultJson.setResultText(faqAnswerText.substring(begin, end));
					return new APIGatewayProxyResponseEvent()
							.withStatusCode(200)
							.withHeaders(corsMap)
							.withBody(cleanUptext(obj.writeValueAsString(resultJson)));
					// return faqAnswerText.substring(begin, end);
				}

			}

			//Otherwise, get first result...
			String firstResultType = results.getResultItems().get(0).getType();

			switch (firstResultType) {
			case QUESTION_ANSWER:
				String faqAnswerText = results.getResultItems().get(0).getDocumentExcerpt().getText();
				resultJson.setResultText(faqAnswerText);
				break;
			case ANSWER:
				List<AdditionalResultAttribute> addList =  results.getResultItems().get(0).getAdditionalAttributes();
				int start_index = 0;
				if(addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().size() > 0) {
					start_index = addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().get(0).getBeginOffset();
				}
				String documentTitle = results.getResultItems().get(0).getDocumentTitle().getText();
				String documentExcerpt = results.getResultItems().get(0).getDocumentExcerpt().getText();
				documentExcerpt = documentExcerpt.substring(start_index);
				String documentUrl = results.getResultItems().get(0).getDocumentURI();
				resultJson.setResultText(documentExcerpt.replace("...", ""));
				resultJson.setDocumentLink(documentUrl);
				resultJson.setDocumentTitle(documentTitle);
				break;
			case DOCUMENT:
				documentTitle = results.getResultItems().get(0).getDocumentTitle().getText();
				documentExcerpt = results.getResultItems().get(0).getDocumentExcerpt().getText();
				documentUrl = results.getResultItems().get(0).getDocumentURI();
				resultJson.setResultText(documentExcerpt.replace("...", ""));
				resultJson.setDocumentLink(documentUrl);
				resultJson.setDocumentTitle(documentTitle);
				break;
			default:
				resultJson.setResultText("Sorry, could not find an answer");

			}

			return new APIGatewayProxyResponseEvent()
					.withStatusCode(200)
					.withHeaders(corsMap)
					.withBody(cleanUptext(obj.writeValueAsString(resultJson)));


			//			if("QUESTION_ANSWER".equals(firstResultType)) {
			//				String faqAnswerText = results.getResultItems().get(0).getDocumentExcerpt().getText();
			//				resultJson.setResultText(faqAnswerText);
			//				return new APIGatewayProxyResponseEvent()
			//						.withStatusCode(200)
			//						.withHeaders(corsMap)
			//						.withBody(cleanUptext(obj.writeValueAsString(resultJson)));
			//			} else if("ANSWER".equals(firstResultType)) {
			//
			//				List<AdditionalResultAttribute> addList =  results.getResultItems().get(0).getAdditionalAttributes();
			//				int start_index = 0;
			//				if(addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().size() > 0) {
			//					start_index = addList.get(0).getValue().getTextWithHighlightsValue().getHighlights().get(0).getBeginOffset();
			//				}
			//
			//				String documentTitle = results.getResultItems().get(0).getDocumentTitle().getText();
			//				String documentExcerpt = results.getResultItems().get(0).getDocumentExcerpt().getText();
			//				documentExcerpt = documentExcerpt.substring(start_index);
			//				String documentUrl = results.getResultItems().get(0).getDocumentURI();
			//
			//				resultJson.setResultText(documentExcerpt.replace("...", ""));
			//				resultJson.setDocumentLink(documentUrl);
			//				resultJson.setDocumentTitle(documentTitle);
			//				return new APIGatewayProxyResponseEvent()
			//						.withStatusCode(200)
			//						.withHeaders(corsMap)
			//						.withBody(cleanUptext(obj.writeValueAsString(resultJson)));
			//
			//			} else if("DOCUMENT".equals(firstResultType)) {
			//				String documentTitle = results.getResultItems().get(0).getDocumentTitle().getText();
			//				String documentExcerpt = results.getResultItems().get(0).getDocumentExcerpt().getText();
			//				String documentUrl = results.getResultItems().get(0).getDocumentURI();
			//
			//				resultJson.setResultText(documentExcerpt.replace("...", ""));
			//				resultJson.setDocumentLink(documentUrl);
			//				resultJson.setDocumentTitle(documentTitle);
			//				return new APIGatewayProxyResponseEvent()
			//						.withStatusCode(200)
			//						.withHeaders(corsMap)
			//						.withBody(cleanUptext(obj.writeValueAsString(resultJson)));
			//			}


		} catch (Exception e) {
			logger.log("*********Error : " + e.getMessage());
			resultJson.setResultText("Error");
			try {
				return new APIGatewayProxyResponseEvent()
						.withStatusCode(500)
						.withHeaders(corsMap)
						.withBody(obj.writeValueAsString(resultJson));

			} catch (JsonProcessingException e1) {
				logger.log(e1.getMessage());
			}
		}

		return new APIGatewayProxyResponseEvent()
				.withStatusCode(200)
				.withHeaders(corsMap)
				.withBody("{\"Kendra Response\":\"None\"}");
	}


	private String cleanUptext(String text) {

		String cleanText = text.replaceAll("\n+", "\n");
		cleanText = cleanText.replaceAll("\t+", " ");

		return cleanText;
	}

	class ResultJson {
		String query;
		String resultText;
		String documentLink;
		String documentTitle;

		public String getQuery() {
			return query;
		}

		public void setQuery(String text) {
			this.query = text;
		}

		public String getResultText() {
			return resultText;
		}

		public void setResultText(String text) {
			this.resultText = text;
		}

		public String getDocumentLink() {
			return documentLink;
		}

		public void setDocumentLink(String link) {
			this.documentLink = link;
		}

		public String getDocumentTitle() {
			return documentTitle;
		}

		public void setDocumentTitle(String title) {
			this.documentTitle = title;
		}

	}

}
