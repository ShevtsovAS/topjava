package ru.javawebinar.topjava.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(value = "/meals", loadOnStartup = 1)
public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private final MealService mealService = new MealServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = StringUtils.trimToEmpty(req.getParameter("action"));
        String page = "/meals.jsp";

        switch (action) {
            case "create":
                page = "/mealForm.jsp";
                break;
            case "edit":
                page = "/mealForm.jsp";
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("meal", mealService.getById(id));
                break;
            case "delete":
                mealService.delete(mealService.getById(Integer.parseInt(req.getParameter("id"))));
                req.setAttribute("meals", mealService.getAll());
                break;
            default:
                req.setAttribute("meals", mealService.getAll());
        }

        req.getRequestDispatcher(page).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");

        String dt = req.getParameter("dt");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dt, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception ex) {
            dateTime = LocalDateTime.now();
        }

        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (id == null || id.isEmpty()) {
            Meal meal = new Meal(dateTime, description, calories);
            mealService.create(meal);
        } else {
            Meal meal = mealService.getById(Integer.parseInt(id));
            meal.setDateTime(dateTime);
            meal.setDescription(description);
            meal.setCalories(calories);
        }

        req.setAttribute("meals", mealService.getAll());
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
