import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TurmaDetailComponent } from './turma-detail.component';

describe('Turma Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TurmaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TurmaDetailComponent,
              resolve: { turma: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(TurmaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load turma on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TurmaDetailComponent);

      // THEN
      expect(instance.turma).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
