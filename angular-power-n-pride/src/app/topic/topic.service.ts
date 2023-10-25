import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { TopicModel } from './topic-response';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  constructor(private http: HttpClient) { }

  getAllTopics(): Observable<Array<TopicModel>> {
    return this.http.get<Array<TopicModel>>('http://localhost:8080/api/topic');
  }

  createTopic(topicModel: TopicModel): Observable<TopicModel> {
    return this.http.post<TopicModel>('http://localhost:8080/api/topic', topicModel);
  }
}
