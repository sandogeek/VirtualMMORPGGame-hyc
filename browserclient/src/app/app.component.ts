import { Component, ViewChild, ElementRef, OnInit, AfterViewInit } from '@angular/core';
import { WebSocketSubject } from 'rxjs/observable/dom/WebSocketSubject';
import { Observable, Observer } from 'rxjs';

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
  webSocket: WebSocket;
  private wsocket: WebSocketSubject<Message>;
  clientMessage = '';
  title = 'browser-client';

  constructor() {
    let destination: Observer<Message> = {
      next: (value: Message) => console.log(value),
      error: (err: any) => console.error(err),
      complete: () => console.warn('Completed!')
    };
    this.wsocket = new WebSocketSubject('ws://localhost:8088/',destination);
    // this.wsocket = new WebSocketSubject('ws://localhost:8088/');

    this.wsocket.subscribe(
        (message) => console.log("subscribe:"+JSON.stringify(message,null,4)),
        (err) => {
          console.log("来自subscribe");
          console.error(err);
        },
        () => console.warn('Completed!')
    );
  }
  public send(): void {
    const message = new Message(10001, this.clientMessage);
    this.wsocket.next(message);
    // this.clientMessage = '';
  }
}
