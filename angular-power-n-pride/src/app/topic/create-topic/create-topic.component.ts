import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { TopicModel } from '../topic-response';
import { Router } from '@angular/router';
import { TopicService } from '../topic.service';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-create-topic',
  templateUrl: './create-topic.component.html',
  styleUrls: ['./create-topic.component.css']
})
export class CreateTopicComponent implements OnInit{

  createTopicForm: FormGroup;
  topicModel: TopicModel;
  title = new FormControl('');
  description = new FormControl('');

  constructor(private router: Router, private topicService: TopicService) {
    this.createTopicForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });
    this.topicModel = {
      name: '',
      description: ''
    }
  }

  ngOnInit() {
  }

  discard() {
    this.router.navigateByUrl('/');
  }

  createTopic() {
    this.topicModel.name = this.createTopicForm.get('title')?.value;
    this.topicModel.description = this.createTopicForm.get('description')?.value;
    this.topicService.createTopic(this.topicModel).subscribe(data => {
      this.router.navigateByUrl('/list-topics');
    }, error => {
      throwError(error);
    })
  }

}
