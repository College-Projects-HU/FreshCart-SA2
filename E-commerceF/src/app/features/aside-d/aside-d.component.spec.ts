import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsideDComponent } from './aside-d.component';

describe('AsideDComponent', () => {
  let component: AsideDComponent;
  let fixture: ComponentFixture<AsideDComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsideDComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AsideDComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
