import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import { HomeComponent } from './home/home.component';
import { CreatePostComponent } from './post/create-post/create-post.component';
import { CreateTopicComponent } from './topic/create-topic/create-topic.component';
import { ListTopicsComponent } from './topic/list-topics/list-topics.component';
import { ViewPostComponent } from './post/view-post/view-post.component';
import { UserProfileComponent } from './auth/user-profile/user-profile.component';
import { ViewTopicComponent } from './topic/view-topic/view-topic.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'view-post/:id', component: ViewPostComponent },
  { path: 'view-topic/:id', component: ViewTopicComponent },
  { path: 'user-profile/:name', component: UserProfileComponent },
  { path: 'list-topics', component: ListTopicsComponent },
  { path: 'create-post', component: CreatePostComponent },
  { path: 'create-topic', component: CreateTopicComponent },
  { path: 'sign-up', component: SignupComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
