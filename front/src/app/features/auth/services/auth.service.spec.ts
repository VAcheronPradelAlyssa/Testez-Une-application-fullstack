import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';
describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // S'assure qu'aucune requête HTTP non gérée ne traîne
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login()', () => {
    it('should call login API with correct data and return SessionInformation', () => {
      const mockRequest: LoginRequest = { email: 'test@test.com', password: 'password' };
      const mockResponse: SessionInformation = {
        token: 'jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'testuser',
        firstName: 'Alice',
        lastName: 'Bob',
        admin: false
      };

      service.login(mockRequest).subscribe(res => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRequest);

      req.flush(mockResponse);
    });

    it('should handle error response', () => {
      const mockRequest: LoginRequest = { email: 'test@test.com', password: 'password' };
      const mockError = { status: 401, statusText: 'Unauthorized' };

      service.login(mockRequest).subscribe({
        next: () => fail('expected an error'),
        error: error => {
          expect(error.status).toBe(401);
          expect(error.statusText).toBe('Unauthorized');
        }
      });

      const req = httpMock.expectOne('api/auth/login');
      req.flush(null, mockError);
    });
  });

  describe('register()', () => {
    it('should call register API with correct data and return void', () => {
      const mockRequest: RegisterRequest = {
        email: 'test@test.com',
        password: 'password',
        firstName: 'Alice',
        lastName: 'Bob'
      };

      service.register(mockRequest).subscribe(res => {
        expect(res).toBeUndefined(); // ou null selon l'API
      });

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRequest);

      req.flush(null);
    });

    it('should handle error response', () => {
      const mockRequest: RegisterRequest = {
        email: 'test@test.com',
        password: 'password',
        firstName: 'Alice',
        lastName: 'Bob'
      };
      const mockError = { status: 400, statusText: 'Bad Request' };

      service.register(mockRequest).subscribe({
        next: () => fail('expected an error'),
        error: error => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe('Bad Request');
        }
      });

      const req = httpMock.expectOne('api/auth/register');
      req.flush(null, mockError);
    });
  });
});
