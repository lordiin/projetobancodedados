import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepartamentoFormService } from './departamento-form.service';
import { DepartamentoService } from '../service/departamento.service';
import { IDepartamento } from '../departamento.model';

import { DepartamentoUpdateComponent } from './departamento-update.component';

describe('Departamento Management Update Component', () => {
  let comp: DepartamentoUpdateComponent;
  let fixture: ComponentFixture<DepartamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departamentoFormService: DepartamentoFormService;
  let departamentoService: DepartamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DepartamentoUpdateComponent],
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
      .overrideTemplate(DepartamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departamentoFormService = TestBed.inject(DepartamentoFormService);
    departamentoService = TestBed.inject(DepartamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const departamento: IDepartamento = { id: 456 };

      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      expect(comp.departamento).toEqual(departamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamento>>();
      const departamento = { id: 123 };
      jest.spyOn(departamentoFormService, 'getDepartamento').mockReturnValue(departamento);
      jest.spyOn(departamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamento }));
      saveSubject.complete();

      // THEN
      expect(departamentoFormService.getDepartamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departamentoService.update).toHaveBeenCalledWith(expect.objectContaining(departamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamento>>();
      const departamento = { id: 123 };
      jest.spyOn(departamentoFormService, 'getDepartamento').mockReturnValue({ id: null });
      jest.spyOn(departamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamento }));
      saveSubject.complete();

      // THEN
      expect(departamentoFormService.getDepartamento).toHaveBeenCalled();
      expect(departamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamento>>();
      const departamento = { id: 123 };
      jest.spyOn(departamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
