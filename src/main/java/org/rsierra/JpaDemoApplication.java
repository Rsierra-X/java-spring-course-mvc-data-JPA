package org.rsierra;

import org.rsierra.models.Category;
import org.rsierra.models.Profile;
import org.rsierra.models.User;
import org.rsierra.models.Vacancy;
import org.rsierra.repository.CategoryRepository;
import org.rsierra.repository.ProfileRepository;
import org.rsierra.repository.UserRepository;
import org.rsierra.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        searchUser();
    }

    private void findVacancyByMultipleStatus() {
        String[] status = new String[] {"Eliminada", "Aprobada"};
        List<Vacancy> lista = vacancyRepository.findByStatusIn(status);
        System.out.println("Registros encontrados: " + lista.size());
        for (Vacancy v : lista) {
            System.out.println(v.getId() + ": " + v.getName() + ": " + v.getStatus());
        }
    }


    private void findVacancyBySalary() {
        List<Vacancy> lista = vacancyRepository.findBySalaryBetweenOrderBySalaryDesc(7000, 14000);
        System.out.println("Registros encontrados: " + lista.size());
        for (Vacancy v : lista) {
            System.out.println(v.getId() + ": " + v.getName() + ": $" + v.getSalary());
        }
    }


    private void findVacancyByFeaturedAndStatus() {
        List<Vacancy> lista = vacancyRepository.findByFeaturedAndStatusOrderByIdDesc(1, "Aprobada");
        System.out.println("Registros encontrados: " + lista.size());
        for (Vacancy v : lista) {
            System.out.println(v.getId() + ": " + v.getName() + ": " + v.getStatus() + ":" + v.getFeatured());
        }
    }

    private void findVacancyByStatus() {
        List<Vacancy> lista = vacancyRepository.findByStatus("Eliminada");
        System.out.println("Registros encontrados: " + lista.size());
        for (Vacancy v : lista) {
            System.out.println(v.getId() + ": " + v.getName() + ": " + v.getStatus());
        }
    }

    public void searchUser() {
        Optional<User> optional = userRepository.findById(1);
        if (optional.isPresent()) {
            User u = optional.get();
            System.out.println("Usuario: " + u.getName());
            System.out.println("Perfiles asignados");
            for (Profile p : u.getProfiles()) {
                System.out.println(p.getProfile());
            }
        }else {
            System.out.println("Usuario no encontrado");
        }
    }

    /*Methods for profiles*/

    private void createUserWithProfiles() {
        User user = new User();
        user.setName("Ivan Tinajero");
        user.setEmail("ivanetinajero@gmail.com");
        user.setRegisterDate(new Date());
        user.setUsername("itinajero");
        user.setPassword("12345");
        user.setStatus(1);

        Profile per1 = new Profile();
        per1.setId(2);

        Profile per2 = new Profile();
        per2.setId(3);

        user.addProfile(per1);
        user.addProfile(per2);

        userRepository.save(user);
    }

    private void createProfilesApplication() {
        profileRepository.saveAll(getProfileApplication());
    }

    /*Methods for Vacancies*/

    private void findAllVacancy(){
        List<Vacancy> vacancies = vacancyRepository.findAll();
        for (Vacancy vacancy : vacancies) {
            System.out.println(vacancy.getId() + " " + vacancy.getName());
        }
    }


    /*Methods for Categories*/

    private void saveAllCategories(){
        List<Category> categories = getListaCategorias();
        categoryRepository.saveAll(categories);
    }

    private void exitedId(){
        boolean exists = categoryRepository.existsById(1);
        if(exists){
            categoryRepository.deleteById(1);
        }
    }

    private void findAllCategoriesSorted(){
        List<Category> categories = categoryRepository.findAll(Sort.by("name").descending());
        for(Category category : categories){
            System.out.println(category);
        }
    }

    private void findAllCategoriesPagination(){
        Page<Category> categoriesPage = categoryRepository.findAll(PageRequest.of(1,5));
        System.out.println("Totoal de paginas"+ categoriesPage.getTotalPages());
        System.out.println("Totoal de elements"+ categoriesPage.getTotalElements());
        for(Category category : categoriesPage.getContent()){
            System.out.println(category);
        }
    }

    private void findAllCategories(){
        /*Iterable<Category> categories = categoryRepository.findAll();*/
        List<Category> categories = categoryRepository.findAll();
        for(Category category : categories){
            System.out.println(category);
        }
    }

     private void findAllById(){
         List<Integer> ids = new LinkedList<>();
         ids.add(1);
         ids.add(2);

         Iterable<Category> categories = categoryRepository.findAllById(ids);
         categories.forEach(System.out::println);
    }

    private void deleteAllCategories() {
        /*categoryRepository.deleteAll();*/
        //This function delete all, take care
        categoryRepository.deleteAllInBatch();
    }

    private void countCategories(){
        long count = categoryRepository.count();
        System.out.println(count);
    }

    private void deleteCategory(){
        int idCategory = 1;
        categoryRepository.deleteById(idCategory);
    }

    private void updateCategory() {
        Optional<Category> category = categoryRepository.findById(1);
        if(category.isPresent()){
            Category auxCategory = category.get();
            auxCategory.setName("New Name");
            auxCategory.setDescription("New description");
            categoryRepository.save(auxCategory);
            System.out.println(category.get().getName());
        } else {
            System.out.println("Categoria no encontrada");
        }
    }

    private void searchById(){
        Optional<Category> category = categoryRepository.findById(1);
        if(category.isPresent()){
            System.out.println(category.get().getName());
        } else {
            System.out.println("Categoria no encontrada");
        }
    }

    private void save(){
        Category category = new Category();
        category.setName("JPA Demo");
        category.setDescription("JPA Demo");
        categoryRepository.save(category);
    }

    private void delete(){
        System.out.println("deleted");
    }

    private List<Category> getListaCategorias(){
        List<Category> lista = new LinkedList<Category>();
        // Categoria 1
        Category cat1 = new Category();
        cat1.setName("Programador de Blockchain");
        cat1.setDescription("Trabajos relacionados con Bitcoin y Criptomonedas");

        // Categoria 2
        Category cat2 = new Category();
        cat2.setName("Soldador/Pintura");
        cat2.setDescription("Trabajos relacionados con soldadura, pintura y enderezado");

        // Categoria 3
        Category cat3 = new Category();
        cat3.setName("Ingeniero Industrial");
        cat3.setDescription("Trabajos relacionados con Ingenieria industrial.");

        lista.add(cat1);
        lista.add(cat2);
        lista.add(cat3);
        return lista;
    }

    private List<Profile> getProfileApplication(){
        List<Profile> list = new LinkedList<Profile>();
        Profile per1 = new Profile();
        per1.setProfile("SUPERVISOR");

        Profile per2 = new Profile();
        per2.setProfile("ADMINISTRADOR");

        Profile per3 = new Profile();
        per3.setProfile("USUARIO");

        list.add(per1);
        list.add(per2);
        list.add(per3);

        return list;
    }
}
