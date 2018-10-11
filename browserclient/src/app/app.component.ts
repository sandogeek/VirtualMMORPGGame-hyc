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
  private wsocket2: WebSocketSubject<Message>;
  clientMessage = '';
  title = 'browser-client';

  constructor() {
    this.wsocket = new WebSocketSubject('ws://localhost:8088');
    this.wsocket2 = new WebSocketSubject('ws://localhost:8083/websocket/?request=e2lkOjE7cmlkOjI2O3Rva2VuOiI0MzYwNjgxMWM3MzA1Y2NjNmFiYjJiZTExNjU3OWJmZCJ9');

    this.wsocket
      .subscribe(
        (message) => console.log(message),
        (err) => console.error(err),
        () => console.warn('Completed!')
      );
    this.wsocket2
      .subscribe(
        (message) => console.log(message),
        (err) => console.error(err),
        () => console.warn('Completed!')
      );
  }
  public send(): void {
    const message = new Message(10001, this.clientMessage);
    this.wsocket.next(message);
    this.wsocket2.next(message);
    this.clientMessage = '';
  }
}
