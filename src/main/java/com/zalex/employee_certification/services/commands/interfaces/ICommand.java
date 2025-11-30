package com.zalex.employee_certification.services.commands.interfaces;

public interface ICommand<T, R> {
    public R execute(T context);
}
