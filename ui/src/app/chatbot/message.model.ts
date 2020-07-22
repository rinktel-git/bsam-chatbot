export enum MESSAGE_TYPE {
    BOT = 'bot',
    USER = 'user'
}

export class messageModel {
    type: MESSAGE_TYPE;
    text: any;
}

