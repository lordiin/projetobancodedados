import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DenunciaService } from '../service/denuncia.service';

import { DenunciaComponent } from './denuncia.component';

describe('Denuncia Management Component', () => {
  let comp: DenunciaComponent;
  let fixture: ComponentFixture<DenunciaComponent>;
  let service: DenunciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'denuncia', component: DenunciaComponent }]),
        HttpClientTestingModule,
        DenunciaComponent,
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
      .overrideTemplate(DenunciaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DenunciaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DenunciaService);

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
    expect(comp.denuncias?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to denunciaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDenunciaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDenunciaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
