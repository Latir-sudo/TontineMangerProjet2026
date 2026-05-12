import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TontineCreate } from './tontine-create';

describe('TontineCreate', () => {
  let component: TontineCreate;
  let fixture: ComponentFixture<TontineCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TontineCreate],
    }).compileComponents();

    fixture = TestBed.createComponent(TontineCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
