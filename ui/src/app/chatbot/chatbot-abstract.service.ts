import { Injectable } from '@angular/core';

@Injectable()
export abstract class ChatbotService {
  /**
   * Abstract service class to return the response from the Chatbot
   *
   * Implement the ChatbotService and pass an instance of your service as the "chatbotService" Input to the chatbot component
   */
  abstract async getChatbotResponse(userQuery: string): Promise<string>;
}
