import { Component, OnInit } from '@angular/core';
import { PostModel } from '../shared/post-model';
import { PostService } from '../shared/post.service';
import { AuthService } from '../auth/shared/auth.service';
import { faSearch } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  isLoggedIn: boolean;
  faSearch = faSearch;
  posts: Array<PostModel> = [];

  constructor(private postService: PostService, private authService: AuthService) {
    this.postService.getAllPosts().subscribe(post => {
      this.posts = post;
    });
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  ngOnInit(): void {
  }
}
