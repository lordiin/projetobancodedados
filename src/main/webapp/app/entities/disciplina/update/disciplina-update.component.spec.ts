import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DisciplinaFormService } from './disciplina-form.service';
import { DisciplinaService } from '../service/disciplina.service';
import { IDisciplina } from '../disciplina.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

import { DisciplinaUpdateComponent } from './disciplina-update.component';

describe('Disciplina Management Update Component', () => {
  let comp: DisciplinaUpdateComponent;
  let fixture: ComponentFixture<DisciplinaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disciplinaFormService: DisciplinaFormService;
  let disciplinaService: DisciplinaService;
  let departamentoService: DepartamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DisciplinaUpdateComponent],
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
      .overrideTemplate(DisciplinaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplinaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disciplinaFormService = TestBed.inject(DisciplinaFormService);
    disciplinaService = TestBed.inject(DisciplinaService);
    departamentoService = TestBed.inject(DepartamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departamento query and add missing value', () => {
      const disciplina: IDisciplina = { id: 456 };
      const departamento: IDepartamento = { id: 90374 };
      disciplina.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 87807 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disciplina });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos.map(expect.objectContaining)
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const disciplina: IDisciplina = { id: 456 };
      const departamento: IDepartamento = { id: 50205 };
      disciplina.departamento = departamento;

      activatedRoute.data = of({ disciplina });
      comp.ngOnInit();

      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.disciplina).toEqual(disciplina);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplina>>();
      const disciplina = { id: 123 };
      jest.spyOn(disciplinaFormService, 'getDisciplina').mockReturnValue(disciplina);
      jest.spyOn(disciplinaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplina });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disciplina }));
      saveSubject.complete();

      // THEN
      expect(disciplinaFormService.getDisciplina).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(disciplinaService.update).toHaveBeenCalledWith(expect.objectContaining(disciplina));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplina>>();
      const disciplina = { id: 123 };
      jest.spyOn(disciplinaFormService, 'getDisciplina').mockReturnValue({ id: null });
      jest.spyOn(disciplinaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplina: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disciplina }));
      saveSubject.complete();

      // THEN
      expect(disciplinaFormService.getDisciplina).toHaveBeenCalled();
      expect(disciplinaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisciplina>>();
      const disciplina = { id: 123 };
      jest.spyOn(disciplinaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disciplina });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disciplinaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
