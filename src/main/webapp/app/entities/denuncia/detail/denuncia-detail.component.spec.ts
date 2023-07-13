import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DenunciaDetailComponent } from './denuncia-detail.component';

describe('Denuncia Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DenunciaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DenunciaDetailComponent,
              resolve: { denuncia: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(DenunciaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load denuncia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DenunciaDetailComponent);

      // THEN
      expect(instance.denuncia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
