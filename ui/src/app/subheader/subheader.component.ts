import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-subheader',
  templateUrl: './subheader.component.html',
  styleUrls: ['./subheader.component.scss']
})
export class SubheaderComponent implements OnInit {

  @Input() buttonNewChatText: string = "Start New Chat";
  @Input() pageTitle: string = "Chatbot";

  @Output() startNewChatEvent: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  startNewChatClicked() {
    this.startNewChatEvent.emit();
  }

}
