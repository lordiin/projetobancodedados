import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DepartamentoDetailComponent } from './departamento-detail.component';

describe('Departamento Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepartamentoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DepartamentoDetailComponent,
              resolve: { departamento: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(DepartamentoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load departamento on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DepartamentoDetailComponent);

      // THEN
      expect(instance.departamento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
