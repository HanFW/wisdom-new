/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Chuck
 */
public class DuplicateEntityException extends Exception {

    public DuplicateEntityException(String message) {
        super(message);
    }
}
