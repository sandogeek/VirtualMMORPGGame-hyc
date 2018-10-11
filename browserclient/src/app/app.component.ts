import { Component, ViewChild, ElementRef, OnInit, AfterViewInit } from '@angular/core';
import { WebSocketSubject } from 'rxjs/observable/dom/WebSocketSubject';

export class Message {
  constructor(
    public packetID: number,
    public content: string,
  ) { }
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private wsocket: WebSocketSubject<Message>;
  clientMessage = '';
  title = 'browser-client';

  constructor() {
    this.wsocket = new WebSocketSubject('ws://localhost:8088/websocket');

    this.wsocket
      .subscribe(
        (message) => console.log(message),
        (err) => console.error(err),
        () => console.warn('Completed!')
      );
  }
  public send(): void {
    const message = new Message(10001, this.clientMessage);
    this.wsocket.next(message);
    // this.clientMessage = '';
  }
}
