import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DenunciaFormService } from './denuncia-form.service';
import { DenunciaService } from '../service/denuncia.service';
import { IDenuncia } from '../denuncia.model';
import { IAvaliacao } from 'app/entities/avaliacao/avaliacao.model';
import { AvaliacaoService } from 'app/entities/avaliacao/service/avaliacao.service';

import { DenunciaUpdateComponent } from './denuncia-update.component';

describe('Denuncia Management Update Component', () => {
  let comp: DenunciaUpdateComponent;
  let fixture: ComponentFixture<DenunciaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let denunciaFormService: DenunciaFormService;
  let denunciaService: DenunciaService;
  let avaliacaoService: AvaliacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DenunciaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DenunciaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DenunciaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    denunciaFormService = TestBed.inject(DenunciaFormService);
    denunciaService = TestBed.inject(DenunciaService);
    avaliacaoService = TestBed.inject(AvaliacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Avaliacao query and add missing value', () => {
      const denuncia: IDenuncia = { id: 456 };
      const avaliacao: IAvaliacao = { id: 76680 };
      denuncia.avaliacao = avaliacao;

      const avaliacaoCollection: IAvaliacao[] = [{ id: 74429 }];
      jest.spyOn(avaliacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: avaliacaoCollection })));
      const additionalAvaliacaos = [avaliacao];
      const expectedCollection: IAvaliacao[] = [...additionalAvaliacaos, ...avaliacaoCollection];
      jest.spyOn(avaliacaoService, 'addAvaliacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ denuncia });
      comp.ngOnInit();

      expect(avaliacaoService.query).toHaveBeenCalled();
      expect(avaliacaoService.addAvaliacaoToCollectionIfMissing).toHaveBeenCalledWith(
        avaliacaoCollection,
        ...additionalAvaliacaos.map(expect.objectContaining)
      );
      expect(comp.avaliacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const denuncia: IDenuncia = { id: 456 };
      const avaliacao: IAvaliacao = { id: 26672 };
      denuncia.avaliacao = avaliacao;

      activatedRoute.data = of({ denuncia });
      comp.ngOnInit();

      expect(comp.avaliacaosSharedCollection).toContain(avaliacao);
      expect(comp.denuncia).toEqual(denuncia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDenuncia>>();
      const denuncia = { id: 123 };
      jest.spyOn(denunciaFormService, 'getDenuncia').mockReturnValue(denuncia);
      jest.spyOn(denunciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ denuncia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: denuncia }));
      saveSubject.complete();

      // THEN
      expect(denunciaFormService.getDenuncia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(denunciaService.update).toHaveBeenCalledWith(expect.objectContaining(denuncia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDenuncia>>();
      const denuncia = { id: 123 };
      jest.spyOn(denunciaFormService, 'getDenuncia').mockReturnValue({ id: null });
      jest.spyOn(denunciaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ denuncia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: denuncia }));
      saveSubject.complete();

      // THEN
      expect(denunciaFormService.getDenuncia).toHaveBeenCalled();
      expect(denunciaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDenuncia>>();
      const denuncia = { id: 123 };
      jest.spyOn(denunciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ denuncia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(denunciaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAvaliacao', () => {
      it('Should forward to avaliacaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(avaliacaoService, 'compareAvaliacao');
        comp.compareAvaliacao(entity, entity2);
        expect(avaliacaoService.compareAvaliacao).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
