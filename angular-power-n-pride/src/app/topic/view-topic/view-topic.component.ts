import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-view-topic',
  templateUrl: './view-topic.component.html',
  styleUrls: ['./view-topic.component.css']
})
export class ViewTopicComponent implements OnInit {

  posts: Array<PostModel> = [];
  postId: number;

  constructor(private postService: PostService, private activateRoute: ActivatedRoute) {
    this.postId = this.activateRoute.snapshot.params['id'];

    this.postService.getAllPostsByTopic(this.postId).subscribe(post => {
      this.posts = post;
    });
  }

  ngOnInit(): void {
  }

}
