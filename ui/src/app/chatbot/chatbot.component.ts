import { Component, OnInit, Input } from '@angular/core';
import { messageModel, MESSAGE_TYPE } from './message.model';
import { ChatbotService, ChatbotResponse } from './chatbot-abstract.service';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.scss']
})
export class ChatbotComponent implements OnInit {

  @Input() chatbotService: ChatbotService;
  @Input() labelMyMessages: string = "Me";
  @Input() labelBotMessages: string = "Bot";
  @Input() defaultChatPlaceholder: string = "Ask me a question...";
  @Input() sendButtonText: string = "Send";
  @Input() initialBotGreeting: string = "<b>Hello!</b> How can I help you today?";

  botServiceErrorMsg: string = "Sorry, the chatbot is unavailable at this time.";
  botResponseErrorMsg: string = "Sorry, the chatbot is unavailable at this time.";

  private userQuestion: string;
  private isLoading: boolean = false;
  public messages: messageModel[] = [];

  constructor() {
  }

  ngOnInit() {
    this.resetChat();
  }

  addUserMessage() {
    this.messages.push({ type: MESSAGE_TYPE.USER, text: this.userQuestion });
    this.userQuestion = '';

    if (this.chatbotService !== undefined && this.chatbotService !== null) {
      this.showBotWaitingMessage();
      setTimeout(() => {
        //Call API
        const res: ChatbotResponse = this.chatbotService.getChatbotResponse();
        let botMsgText = res.responseHTML;
        this.hideBotWaitingMessage();
        if (botMsgText !== undefined && botMsgText != null) {
          this.addBotMessage(botMsgText);
        } else {
          this.addBotMessage(this.botResponseErrorMsg);
        }
      },
        2000);
    } else {
      this.addBotMessage(this.botServiceErrorMsg);
    }
  }

  addBotMessage(msgText) {
    this.messages.push({ type: MESSAGE_TYPE.BOT, text: msgText });
  }

  showBotWaitingMessage() {
    this.isLoading = true;
  }

  hideBotWaitingMessage() {
    this.isLoading = false;
  }

  public startNewChat() {
    this.resetChat();
  }

  resetChat() {
    this.userQuestion = '';
    this.messages = [];
    this.messages.push({ type: MESSAGE_TYPE.BOT, text: this.initialBotGreeting });
  }

}
