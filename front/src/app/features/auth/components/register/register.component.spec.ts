import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from "../../services/auth.service";
import { throwError } from "rxjs";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [AuthService]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should form value be of type RegisterRequest', () => {
    const expectedEmail = 'test@test.com';
    const expectedFirstName = 'firstName';
    const expectedLastName = 'lastName';
    const expectedPassword = 'password';

    component.form.controls['email'].setValue(expectedEmail);
    component.form.controls['firstName'].setValue(expectedFirstName);
    component.form.controls['lastName'].setValue(expectedLastName);
    component.form.controls['password'].setValue(expectedPassword);

    component.submit();

    const registerRequest = component.form.value;
    expect(registerRequest).toEqual({
      email: expectedEmail,
      firstName: expectedFirstName,
      lastName: expectedLastName,
      password: expectedPassword
    });
  });

  it('Should set onError to true when register fails', () => {
    const authService = TestBed.inject(AuthService);
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => 'Error'));

    component.submit();

    expect(component.onError).toBe(true);
  });

});