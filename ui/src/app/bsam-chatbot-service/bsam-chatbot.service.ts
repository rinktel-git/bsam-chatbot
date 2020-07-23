import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChatbotService } from '../chatbot/chatbot-abstract.service';
import { BSAMChatbotAPIResponse } from './bsam-chatbot.model';
import { catchError } from 'rxjs/operators';

@Injectable()
export class BSAMChatbotService implements ChatbotService {

  // https://17b6zypkbe.execute-api.us-east-1.amazonaws.com/dev/kendra?queryText=What%20is%20api?
  private static API_URL = 'https://17b6zypkbe.execute-api.us-east-1.amazonaws.com/dev/kendra';

  constructor(private http: HttpClient) {
  }

  async getChatbotResponse(userQuery: string): Promise<string> {
    if (!this.hasValue(userQuery)) {
      return Promise.reject('User Query must have a value');
    }

    const options = { params: { queryText: userQuery } };
    const apiResponse = await this.http.get<BSAMChatbotAPIResponse>(BSAMChatbotService.API_URL, options).pipe(
      catchError((err) => {
        return Promise.reject(err);
      })
    ).toPromise();

    // console.log('DATA FROM API', apiResponse);
    if (apiResponse === undefined || apiResponse == null) {
      return Promise.reject('Error receiving response. Response is null');
    }

    const strResponse = this.buildResponseText(apiResponse);
    return Promise.resolve(strResponse);
  }

  private buildResponseText(apiResponse: BSAMChatbotAPIResponse): string {
    let strResponse = '';

    // Text
    if (this.hasValue(apiResponse.resultText)) {
      const text = this.stripExtraNewlines(apiResponse.resultText);
      strResponse += text;
      if (this.hasValue(apiResponse.documentLink)) {
        // Indicates an excerpt, so add ... at the end
        strResponse += '...';
      }
    }

    // Document
    if (this.hasValue(apiResponse.documentLink)) {
      if (strResponse.length > 0) {
        strResponse += '\n';
      }

      const info = 'Please open the document below for more information:';
      const docLink = apiResponse.documentLink;
      const docTitle = this.hasValue(apiResponse.documentTitle) ? apiResponse.documentTitle : 'External Link';

      // tslint:disable-next-line: max-line-length
      const template = `<div class="usa-link-wrapper"><p class="text-italic">${info}</p><p><a class="usa-link" href="${docLink}" target="_blank">${docTitle}</a></p></div>`;
      // <i class="fas fa-external-link-alt fa-xs"></i></a>
      // <fa-icon [icon]="['fas', 'external-link-alt']" size="xs">

      strResponse += template;

    } else if (this.hasValue(apiResponse.documentTitle)) {
      if (strResponse.length > 0) {
        strResponse += '\n';
      }
      strResponse += apiResponse.documentTitle;
    }
    return strResponse;
  }

  private hasValue(str: string) {
    return str !== undefined && str != null && str.length > 0;
  }

  private stripExtraNewlines(text) {
    // Replace two newlines with one newline
    return text.replace(/\n{2,}/g, '\n');​​​​​​​
  }
}
