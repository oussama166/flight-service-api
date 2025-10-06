package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetblue.jetblue.Models.ENUM.BaggageType;

/**
 * Représente le catalogue de référence des types de bagages autorisés et de leurs caractéristiques physiques,
 * tel que défini par la compagnie aérienne (Airline). Cette entité ne contient pas de prix commercial,
 * car le prix dépendra du vol et de l'offre associée.
 *
 * <p>
 * Fonctionnellement, ce catalogue garantit la cohérence opérationnelle : lorsqu'un client achète un item,
 * il sait que cet item a une limite de poids ou de taille définie et standardisée par la compagnie.
 * Par exemple :
 * <ul>
 *   <li>ID 1 : Valise Standard (max 25kg)</li>
 *   <li>ID 2 : Sac à dos Cabine (max 8kg)</li>
 *   <li>ID 3 : Équipement de golf</li>
 * </ul>
 * </p>
 *
 * <p>
 * Règle métier : Si le poids maximal d'un bagage "standard" change, la modification se fait uniquement ici,
 * assurant ainsi la centralisation et la cohérence des règles de gestion des bagages.
 * </p>
 *
 * @author oussama166
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "catalog_baggage")
public class CatalogBaggage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;

  @Column(unique = true)
  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private BaggageType baggageType;

  private Double maxWeightKg;
  private String dimensions; // e.g., "22x14x9 inches"

  @OneToMany(
    mappedBy = "catalogBaggage",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Airline> airline;

  private LocalDate createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDate.now();
    this.code =
      "BG-" +
      baggageType.toString().substring(0, 2).toUpperCase() +
      "-" +
      System.currentTimeMillis();
  }
}
