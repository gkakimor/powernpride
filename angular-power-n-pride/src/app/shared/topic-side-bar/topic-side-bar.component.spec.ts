import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicSideBarComponent } from './topic-side-bar.component';

describe('TopicSideBarComponent', () => {
  let component: TopicSideBarComponent;
  let fixture: ComponentFixture<TopicSideBarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TopicSideBarComponent]
    });
    fixture = TestBed.createComponent(TopicSideBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
