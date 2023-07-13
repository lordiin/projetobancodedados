import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDenuncia } from '../denuncia.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../denuncia.test-samples';

import { DenunciaService } from './denuncia.service';

const requireRestSample: IDenuncia = {
  ...sampleWithRequiredData,
};

describe('Denuncia Service', () => {
  let service: DenunciaService;
  let httpMock: HttpTestingController;
  let expectedResult: IDenuncia | IDenuncia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DenunciaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Denuncia', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const denuncia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(denuncia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Denuncia', () => {
      const denuncia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(denuncia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Denuncia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Denuncia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Denuncia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDenunciaToCollectionIfMissing', () => {
      it('should add a Denuncia to an empty array', () => {
        const denuncia: IDenuncia = sampleWithRequiredData;
        expectedResult = service.addDenunciaToCollectionIfMissing([], denuncia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(denuncia);
      });

      it('should not add a Denuncia to an array that contains it', () => {
        const denuncia: IDenuncia = sampleWithRequiredData;
        const denunciaCollection: IDenuncia[] = [
          {
            ...denuncia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDenunciaToCollectionIfMissing(denunciaCollection, denuncia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Denuncia to an array that doesn't contain it", () => {
        const denuncia: IDenuncia = sampleWithRequiredData;
        const denunciaCollection: IDenuncia[] = [sampleWithPartialData];
        expectedResult = service.addDenunciaToCollectionIfMissing(denunciaCollection, denuncia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(denuncia);
      });

      it('should add only unique Denuncia to an array', () => {
        const denunciaArray: IDenuncia[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const denunciaCollection: IDenuncia[] = [sampleWithRequiredData];
        expectedResult = service.addDenunciaToCollectionIfMissing(denunciaCollection, ...denunciaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const denuncia: IDenuncia = sampleWithRequiredData;
        const denuncia2: IDenuncia = sampleWithPartialData;
        expectedResult = service.addDenunciaToCollectionIfMissing([], denuncia, denuncia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(denuncia);
        expect(expectedResult).toContain(denuncia2);
      });

      it('should accept null and undefined values', () => {
        const denuncia: IDenuncia = sampleWithRequiredData;
        expectedResult = service.addDenunciaToCollectionIfMissing([], null, denuncia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(denuncia);
      });

      it('should return initial array if no Denuncia is added', () => {
        const denunciaCollection: IDenuncia[] = [sampleWithRequiredData];
        expectedResult = service.addDenunciaToCollectionIfMissing(denunciaCollection, undefined, null);
        expect(expectedResult).toEqual(denunciaCollection);
      });
    });

    describe('compareDenuncia', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDenuncia(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDenuncia(entity1, entity2);
        const compareResult2 = service.compareDenuncia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDenuncia(entity1, entity2);
        const compareResult2 = service.compareDenuncia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDenuncia(entity1, entity2);
        const compareResult2 = service.compareDenuncia(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
