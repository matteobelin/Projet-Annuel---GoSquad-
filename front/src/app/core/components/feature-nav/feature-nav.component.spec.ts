import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeatureNavComponent } from './feature-nav.component';

describe('FeatureNavComponent', () => {
  let component: FeatureNavComponent;
  let fixture: ComponentFixture<FeatureNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeatureNavComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeatureNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
