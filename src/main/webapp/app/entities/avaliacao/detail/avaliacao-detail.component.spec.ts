import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AvaliacaoDetailComponent } from './avaliacao-detail.component';

describe('Avaliacao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliacaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AvaliacaoDetailComponent,
              resolve: { avaliacao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(AvaliacaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load avaliacao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AvaliacaoDetailComponent);

      // THEN
      expect(instance.avaliacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
