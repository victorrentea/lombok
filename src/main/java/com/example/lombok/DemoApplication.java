package com.example.lombok;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class DemoApplication {

   public static void main(String[] args) {
//      SpringApplication.run(DemoApplication.class, args);
      User user = new User();
//      user.setFirstName("John");


//      new FullNameMutable.FullNameMutableBuilder()
//          .firstName("a")
//          .lastName("b")
//          .build();

      new FullNameMutable()
          .setFirstName("f")
          .setLastName("n");

      try {
         met();
      } catch (Exception e) {
         e.printStackTrace();
      }

      Permission p = new Permission();
      user.getPermissions().add(p);
//      user.setPermissions(Arrays.asList(p));

      System.out.println(user);
   }
//
//   @SneakyThrows
   private static void met() {
      try (FileWriter writer = new FileWriter("f.txt")) {
         writer.write("aaa");
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }


//   private static void met() {
//      try {
//         FileWriter writer = new FileWriter("f.txt");
//         Throwable var1 = null;
//
//         try {
//            writer.write("aaa");
//         } catch (Throwable var11) {
//            var1 = var11;
//            throw var11;
//         } finally {
//            if (writer != null) {
//               if (var1 != null) {
//                  try {
//                     writer.close();
//                  } catch (Throwable var10) {
//                     var1.addSuppressed(var10);
//                  }
//               } else {
//                  writer.close();
//               }
//            }
//
//         }
//
//      } catch (Throwable var13) {
//         throw var13;
//      }
//   }

}
@Slf4j
@Component
@RequiredArgsConstructor
class MyService {
//   private static final Logger log = LoggerFactory.getLogger(MyService.class);
   private final OtherService otherService;
   private final UserRepo userRepo;

   @org.springframework.beans.factory.annotation.Value("${in.folder.path}")
   private File inFolder;


   public void method() {
      log.debug("magic!");
   }

}
@RequiredArgsConstructor
@Component
class OtherService {
   private final MyService service;

}
interface UserRepo extends JpaRepository<User, Long> {
}



//@Setter
//@Getter
//@ToString
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)

//very good for immutable objects
@Data
@Builder
class FullName {
   @NonNull
   private final String firstName;
   private final String lastName;

   public FullName(@NonNull String firstName,  String lastName) {
      
      this.firstName = firstName;
      this.lastName = Objects.requireNonNull(lastName);
   }
}

@Data
class FullNameMutable {
   private String firstName;
   private String lastName;
}


@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
class FullName3 {
   private String firstName;
   private String lastName;
}

//@Data // bad practice
@ToString
@Entity
@Getter
@Setter
class User {
   @Id
   @GeneratedValue
   @Setter(AccessLevel.NONE)
   private Long id;

   @Embedded
   private FullName3 fullName;
   private String phone;
   @ToString.Exclude
   @OneToMany(mappedBy = "user")
   @Setter(AccessLevel.NONE)
   private List<Permission> permissions = new ArrayList<>(); // Hibernate lazy load

   public List<Permission> getPermissions() {
      return Collections.unmodifiableList(permissions);
   }

   public void addPermission(Permission permission) {
      permissions.add(permission);
      permission.setUser(this);
   }
}

// "SELECT new com.example.lombok.UserSearchResult(u.id, u.firstName, u.lastName) FROM User u WHERE....

@Value
class UserSearchResult {
   long id;
   String firstName;
   String lastName;
}

@ToString
@Entity
class Permission {
   @Id
   private Long id;
   @ManyToOne
   private User user;

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }
}