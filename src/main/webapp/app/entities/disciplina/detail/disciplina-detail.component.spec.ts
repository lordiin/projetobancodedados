import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisciplinaDetailComponent } from './disciplina-detail.component';

describe('Disciplina Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisciplinaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DisciplinaDetailComponent,
              resolve: { disciplina: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(DisciplinaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load disciplina on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DisciplinaDetailComponent);

      // THEN
      expect(instance.disciplina).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
