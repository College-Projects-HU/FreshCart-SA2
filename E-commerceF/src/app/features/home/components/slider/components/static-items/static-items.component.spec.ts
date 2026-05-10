import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticItemsComponent } from './static-items.component';

describe('StaticItemsComponent', () => {
  let component: StaticItemsComponent;
  let fixture: ComponentFixture<StaticItemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaticItemsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(StaticItemsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
