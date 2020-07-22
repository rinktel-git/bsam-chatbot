import { Injectable } from '@angular/core';

export interface ChatbotResponse {
    responseHTML: string;
}

@Injectable()
export abstract class ChatbotService {
  /**
   * Returns response from Chatbot
   */
  abstract getChatbotResponse(): ChatbotResponse;
}