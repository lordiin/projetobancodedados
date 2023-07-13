import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AvaliacaoFormService } from './avaliacao-form.service';
import { AvaliacaoService } from '../service/avaliacao.service';
import { IAvaliacao } from '../avaliacao.model';
import { ITurma } from 'app/entities/turma/turma.model';
import { TurmaService } from 'app/entities/turma/service/turma.service';

import { AvaliacaoUpdateComponent } from './avaliacao-update.component';

describe('Avaliacao Management Update Component', () => {
  let comp: AvaliacaoUpdateComponent;
  let fixture: ComponentFixture<AvaliacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let avaliacaoFormService: AvaliacaoFormService;
  let avaliacaoService: AvaliacaoService;
  let turmaService: TurmaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AvaliacaoUpdateComponent],
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
      .overrideTemplate(AvaliacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AvaliacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    avaliacaoFormService = TestBed.inject(AvaliacaoFormService);
    avaliacaoService = TestBed.inject(AvaliacaoService);
    turmaService = TestBed.inject(TurmaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Turma query and add missing value', () => {
      const avaliacao: IAvaliacao = { id: 456 };
      const turma: ITurma = { id: 89953 };
      avaliacao.turma = turma;

      const turmaCollection: ITurma[] = [{ id: 86688 }];
      jest.spyOn(turmaService, 'query').mockReturnValue(of(new HttpResponse({ body: turmaCollection })));
      const additionalTurmas = [turma];
      const expectedCollection: ITurma[] = [...additionalTurmas, ...turmaCollection];
      jest.spyOn(turmaService, 'addTurmaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ avaliacao });
      comp.ngOnInit();

      expect(turmaService.query).toHaveBeenCalled();
      expect(turmaService.addTurmaToCollectionIfMissing).toHaveBeenCalledWith(
        turmaCollection,
        ...additionalTurmas.map(expect.objectContaining)
      );
      expect(comp.turmasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const avaliacao: IAvaliacao = { id: 456 };
      const turma: ITurma = { id: 90578 };
      avaliacao.turma = turma;

      activatedRoute.data = of({ avaliacao });
      comp.ngOnInit();

      expect(comp.turmasSharedCollection).toContain(turma);
      expect(comp.avaliacao).toEqual(avaliacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvaliacao>>();
      const avaliacao = { id: 123 };
      jest.spyOn(avaliacaoFormService, 'getAvaliacao').mockReturnValue(avaliacao);
      jest.spyOn(avaliacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avaliacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avaliacao }));
      saveSubject.complete();

      // THEN
      expect(avaliacaoFormService.getAvaliacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(avaliacaoService.update).toHaveBeenCalledWith(expect.objectContaining(avaliacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvaliacao>>();
      const avaliacao = { id: 123 };
      jest.spyOn(avaliacaoFormService, 'getAvaliacao').mockReturnValue({ id: null });
      jest.spyOn(avaliacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avaliacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: avaliacao }));
      saveSubject.complete();

      // THEN
      expect(avaliacaoFormService.getAvaliacao).toHaveBeenCalled();
      expect(avaliacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAvaliacao>>();
      const avaliacao = { id: 123 };
      jest.spyOn(avaliacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ avaliacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(avaliacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTurma', () => {
      it('Should forward to turmaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(turmaService, 'compareTurma');
        comp.compareTurma(entity, entity2);
        expect(turmaService.compareTurma).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
