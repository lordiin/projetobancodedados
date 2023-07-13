import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TurmaFormService } from './turma-form.service';
import { TurmaService } from '../service/turma.service';
import { ITurma } from '../turma.model';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

import { TurmaUpdateComponent } from './turma-update.component';

describe('Turma Management Update Component', () => {
  let comp: TurmaUpdateComponent;
  let fixture: ComponentFixture<TurmaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let turmaFormService: TurmaFormService;
  let turmaService: TurmaService;
  let disciplinaService: DisciplinaService;
  let departamentoService: DepartamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TurmaUpdateComponent],
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
      .overrideTemplate(TurmaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TurmaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    turmaFormService = TestBed.inject(TurmaFormService);
    turmaService = TestBed.inject(TurmaService);
    disciplinaService = TestBed.inject(DisciplinaService);
    departamentoService = TestBed.inject(DepartamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Disciplina query and add missing value', () => {
      const turma: ITurma = { id: 456 };
      const disciplina: IDisciplina = { id: 73589 };
      turma.disciplina = disciplina;

      const disciplinaCollection: IDisciplina[] = [{ id: 55004 }];
      jest.spyOn(disciplinaService, 'query').mockReturnValue(of(new HttpResponse({ body: disciplinaCollection })));
      const additionalDisciplinas = [disciplina];
      const expectedCollection: IDisciplina[] = [...additionalDisciplinas, ...disciplinaCollection];
      jest.spyOn(disciplinaService, 'addDisciplinaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      expect(disciplinaService.query).toHaveBeenCalled();
      expect(disciplinaService.addDisciplinaToCollectionIfMissing).toHaveBeenCalledWith(
        disciplinaCollection,
        ...additionalDisciplinas.map(expect.objectContaining)
      );
      expect(comp.disciplinasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const turma: ITurma = { id: 456 };
      const departamento: IDepartamento = { id: 75446 };
      turma.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 59330 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos.map(expect.objectContaining)
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const turma: ITurma = { id: 456 };
      const disciplina: IDisciplina = { id: 82863 };
      turma.disciplina = disciplina;
      const departamento: IDepartamento = { id: 99981 };
      turma.departamento = departamento;

      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      expect(comp.disciplinasSharedCollection).toContain(disciplina);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.turma).toEqual(turma);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITurma>>();
      const turma = { id: 123 };
      jest.spyOn(turmaFormService, 'getTurma').mockReturnValue(turma);
      jest.spyOn(turmaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: turma }));
      saveSubject.complete();

      // THEN
      expect(turmaFormService.getTurma).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(turmaService.update).toHaveBeenCalledWith(expect.objectContaining(turma));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITurma>>();
      const turma = { id: 123 };
      jest.spyOn(turmaFormService, 'getTurma').mockReturnValue({ id: null });
      jest.spyOn(turmaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turma: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: turma }));
      saveSubject.complete();

      // THEN
      expect(turmaFormService.getTurma).toHaveBeenCalled();
      expect(turmaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITurma>>();
      const turma = { id: 123 };
      jest.spyOn(turmaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ turma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(turmaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDisciplina', () => {
      it('Should forward to disciplinaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(disciplinaService, 'compareDisciplina');
        comp.compareDisciplina(entity, entity2);
        expect(disciplinaService.compareDisciplina).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartamento', () => {
      it('Should forward to departamentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departamentoService, 'compareDepartamento');
        comp.compareDepartamento(entity, entity2);
        expect(departamentoService.compareDepartamento).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
