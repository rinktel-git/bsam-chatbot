package com.bsam.chatbot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.services.kendra.KendraClient;
import software.amazon.awssdk.services.kendra.model.QueryRequest;
import software.amazon.awssdk.services.kendra.model.QueryResponse;
import software.amazon.awssdk.services.kendra.model.QueryResultItem;

import java.util.Collections;

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

        KendraClient kendra = KendraClient.builder().build();

        String response = null;
        String query = input.getQueryStringParameters().get("queryText");
        String indexId = "b44f2d70-0554-4cbf-871a-9a88bbe04c7c";

        QueryRequest queryRequest = QueryRequest
                .builder()
                .queryText(query)
                .indexId(indexId)
                .build();

        QueryResponse queryResponse = kendra.query(queryRequest);

        logger.log(String.format("\nSearch results for query: %s", query));

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(Collections.emptyMap())
                .withBody("{\"Kendra Response\":\"" + response + "\"}");
    }
}
