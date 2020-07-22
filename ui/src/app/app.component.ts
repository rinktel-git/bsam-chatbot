import { Component, ViewChild } from '@angular/core';
import { ChatbotComponent } from './chatbot/chatbot.component';
import { BSAMChatbotService } from './bsam-chatbot-service/bsam-chatbot.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  
  //subheader
  title = 'API Chatbot';
  buttonNewChatText = 'Start New Chat';

  //chatbot
  chatbotService = new BSAMChatbotService();
  labelMyMessages: string = "Me";
  labelBotMessages: string = "API Bot";
  defaultChatPlaceholder: string = "Ask me a question...";
  sendButtonText: string = "Send";
  initialBotGreeting: string = "<b>Hello!</b> How can I help you today?";

  @ViewChild(ChatbotComponent, {static: false}) chatbot;

  startNewChat() {
    this.chatbot.startNewChat();
  }
}

