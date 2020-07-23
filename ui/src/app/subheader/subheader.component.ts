import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-subheader',
  templateUrl: './subheader.component.html',
  styleUrls: ['./subheader.component.scss']
})
export class SubheaderComponent implements OnInit {

  /** Subheader button text */
  @Input() buttonText;

  /** Page title */
  @Input() pageTitle;

  @Output() buttonClickedEvent: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  buttonClicked() {
    this.buttonClickedEvent.emit();
  }

}
