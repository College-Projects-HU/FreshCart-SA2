import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecificComponent } from './specific.component';

describe('SpecificComponent', () => {
  let component: SpecificComponent;
  let fixture: ComponentFixture<SpecificComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpecificComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SpecificComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
