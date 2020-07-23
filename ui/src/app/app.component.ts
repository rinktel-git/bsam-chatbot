import { Component, ViewChild } from '@angular/core';
import { ChatbotComponent } from './chatbot/chatbot.component';
import { BSAMChatbotService } from './bsam-chatbot-service/bsam-chatbot.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  // subheader inputs
  title = 'beta.SAM Chatbot';
  buttonNewChatText = 'Start New Chat';

  // chatbot inputs
  chatbotService = new BSAMChatbotService(this.http);
  labelMyMessages = 'Me';
  labelBotMessages = 'BSAM Bot';
  defaultChatPlaceholder = 'Ask me a question...';
  sendButtonText = 'Send';
  initialBotGreeting = '<b>Hello!</b> How can I help you today?';
  botServiceErrorMsg = 'Sorry, the chatbot is unavailable at this time.';
  botResponseErrorMsg = 'Sorry, the chatbot is unavailable at this time.';

  @ViewChild(ChatbotComponent, {static: false}) chatbot;

  constructor(private http: HttpClient) {
  }

  startNewChat() {
    this.chatbot.startNewChat();
  }
}

