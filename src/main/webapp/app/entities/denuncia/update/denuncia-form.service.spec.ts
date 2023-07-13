import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../denuncia.test-samples';

import { DenunciaFormService } from './denuncia-form.service';

describe('Denuncia Form Service', () => {
  let service: DenunciaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DenunciaFormService);
  });

  describe('Service methods', () => {
    describe('createDenunciaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDenunciaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            motivo: expect.any(Object),
            avaliacao: expect.any(Object),
          })
        );
      });

      it('passing IDenuncia should create a new form with FormGroup', () => {
        const formGroup = service.createDenunciaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            motivo: expect.any(Object),
            avaliacao: expect.any(Object),
          })
        );
      });
    });

    describe('getDenuncia', () => {
      it('should return NewDenuncia for default Denuncia initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDenunciaFormGroup(sampleWithNewData);

        const denuncia = service.getDenuncia(formGroup) as any;

        expect(denuncia).toMatchObject(sampleWithNewData);
      });

      it('should return NewDenuncia for empty Denuncia initial value', () => {
        const formGroup = service.createDenunciaFormGroup();

        const denuncia = service.getDenuncia(formGroup) as any;

        expect(denuncia).toMatchObject({});
      });

      it('should return IDenuncia', () => {
        const formGroup = service.createDenunciaFormGroup(sampleWithRequiredData);

        const denuncia = service.getDenuncia(formGroup) as any;

        expect(denuncia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDenuncia should not enable id FormControl', () => {
        const formGroup = service.createDenunciaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDenuncia should disable id FormControl', () => {
        const formGroup = service.createDenunciaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
