import { Component, OnInit, Input } from '@angular/core';
import { MessageModel, MESSAGE_TYPE } from './message.model';
import { ChatbotService } from './chatbot-abstract.service';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.scss']
})
export class ChatbotComponent implements OnInit {

  /** Instance of a ChatbotService which returns the Chatbot response to the user query */
  @Input() chatbotService: ChatbotService;

  /** Label for the user's messages */
  @Input() labelMyMessages = 'Me';

  /** Label for the chatbot's messages */
  @Input() labelBotMessages = 'Bot';

  /** Initial placeholder in the messages input box */
  @Input() defaultChatPlaceholder = 'Ask me a question...';

  /** Button text to send message to chatbot */
  @Input() sendButtonText = 'Send';

  /** Initial chatbot greeting at start of conversation */
  @Input() initialBotGreeting = '<b>Hello!</b> How can I help you today?';

  /** Error message for a chatbot service error */
  @Input() botServiceErrorMsg = 'Sorry, the chatbot is unavailable at this time.';

  /** Error message for a chatbot response error */
  @Input() botResponseErrorMsg = 'Sorry, the chatbot is unavailable at this time.';

  private userQuestion: string;
  private isLoading = false;
  public messages: MessageModel[] = [];

  constructor() {
  }

  ngOnInit() {
    this.resetChat();
  }

  addUserMessage() {
    const userQuery = this.userQuestion;
    this.messages.push({ type: MESSAGE_TYPE.USER, text: userQuery });
    this.userQuestion = '';

    if (this.chatbotService !== undefined && this.chatbotService != null) {
      this.showBotWaitingMessage();

      this.chatbotService.getChatbotResponse(userQuery).then(
        (botMsgText) => {
          // Success
          this.hideBotWaitingMessage();
          if (botMsgText !== undefined && botMsgText != null) {
            this.addBotMessage(botMsgText);
          } else {
            this.addBotMessage(this.botResponseErrorMsg);
          }
        },
        (err) => {
          // Failure
          this.hideBotWaitingMessage();
          this.addBotMessage(this.botResponseErrorMsg);
        },
      );
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
