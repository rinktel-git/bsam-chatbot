package com.bsam.chatbot;

import java.util.Collections;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kendra.AWSkendra;
import com.amazonaws.services.kendra.AWSkendraClientBuilder;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HandlerKendra implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        LambdaLogger logger = context.getLogger();
       

        // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        logger.log("CONTEXT: " + gson.toJson(context));
        // process input
        logger.log("EVENT: " + gson.toJson(input));
        logger.log("EVENT TYPE: " + input.getClass().toString());

        String queryText = input.getQueryStringParameters().get("queryText");
        
        ResultJson resultJson = new ResultJson();
		ObjectMapper obj = new ObjectMapper();
		resultJson.setQuery(queryText);
		try {
//        KendraClient kendra = KendraClient.builder().build();
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv().get("AWS_ACCESS_KEY_ID"), System.getenv().get("AWS_SECRET_KEY"));
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
	                .withHeaders(Collections.emptyMap())
	                .withBody(obj.writeValueAsString(resultJson));
		}
		
		for(QueryResultItem type : results.getResultItems()) {


			if("QUESTION_ANSWER".equals(type.getType())) {
				String faqAnswerText = type.getDocumentExcerpt().getText();
				resultJson.setResultText(faqAnswerText);
				return new APIGatewayProxyResponseEvent()
		                .withStatusCode(200)
		                .withHeaders(Collections.emptyMap())
		                .withBody(obj.writeValueAsString(resultJson));
			}

		}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			resultJson.setResultText("Error");
			try {
				return new APIGatewayProxyResponseEvent()
		                .withStatusCode(500)
		                .withHeaders(Collections.emptyMap())
		                .withBody(obj.writeValueAsString(resultJson));
				
			} catch (JsonProcessingException e1) {
				System.out.println(e1);
			}
		}
		

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(Collections.emptyMap())
                .withBody("{\"Kendra Response\":\"None\"}");
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
