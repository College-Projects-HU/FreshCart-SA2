import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TwoStaticComponent } from './two-static.component';

describe('TwoStaticComponent', () => {
  let component: TwoStaticComponent;
  let fixture: ComponentFixture<TwoStaticComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TwoStaticComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TwoStaticComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
