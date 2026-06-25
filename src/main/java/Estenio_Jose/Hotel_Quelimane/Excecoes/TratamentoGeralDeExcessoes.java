package Estenio_Jose.Hotel_Quelimane.Excecoes;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TratamentoGeralDeExcessoes {

    @ExceptionHandler(Exception.class)
    public String handleGenericException(
            Exception ex,
            Model model,
            HttpServletRequest request) {

        // Registrar o erro no console
        ex.printStackTrace();

        model.addAttribute("titulo", "Ops! Algo deu errado 😕");
        model.addAttribute(
                "mensagem",
                "Ocorreu um problema inesperado. Tente novamente mais tarde."
        );

        model.addAttribute("pagina", request.getRequestURI());

        // Mostrar detalhe apenas durante desenvolvimento
        model.addAttribute("detalhe", ex.getMessage());

        return "erro";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(
            IllegalArgumentException ex,
            Model model) {

        model.addAttribute("titulo", "Dados inválidos ⚠️");
        model.addAttribute("mensagem", ex.getMessage());

        return "erro";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(
            RuntimeException ex,
            Model model) {

        model.addAttribute("titulo", "Erro de operação ⚠️");
        model.addAttribute("mensagem", ex.getMessage());

        return "erro";
    }
}