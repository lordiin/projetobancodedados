import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisciplinaService } from '../service/disciplina.service';

import { DisciplinaComponent } from './disciplina.component';

describe('Disciplina Management Component', () => {
  let comp: DisciplinaComponent;
  let fixture: ComponentFixture<DisciplinaComponent>;
  let service: DisciplinaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'disciplina', component: DisciplinaComponent }]),
        HttpClientTestingModule,
        DisciplinaComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(DisciplinaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplinaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DisciplinaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.disciplinas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to disciplinaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDisciplinaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDisciplinaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
