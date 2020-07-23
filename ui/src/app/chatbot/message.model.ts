export enum MESSAGE_TYPE {
    BOT = 'bot',
    USER = 'user'
}

export class MessageModel {
    type: MESSAGE_TYPE;
    text: any;
}

