package com.zalex.employee_certification.services.handlers.interfaces;

public interface IHandler<T> {
    public void setNext(IHandler<T> next);
    public boolean hasNext();
    public void handle(T context);
}
