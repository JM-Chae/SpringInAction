package com.example.tacocloud.data;

import com.example.tacocloud.Ingredient;
import com.example.tacocloud.Taco;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
@Log4j2
public class JdbcTacoRepository implements TacoRepository
  {
    private final JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc)
      {
        this.jdbc = jdbc;
      }

    @Override
    public Taco save(Taco taco)
      {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for( Ingredient ingredient : taco.getIngredients()) {
          saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
      }

    private long saveTacoInfo(Taco taco)
      {
        taco.setCreatedAt(new Date());
        PreparedStatementCreator psc = new PreparedStatementCreatorFactory
            (
                "insert into Taco (name, createdAt) values ( ?,? )", Types.VARCHAR, Types.TIMESTAMP
            )
            .newPreparedStatementCreator(Arrays.asList(taco.getName(),
                new Timestamp(taco.getCreatedAt().getTime())));


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        log.info(keyHolder);
        log.info(psc);
        log.info(jdbc.update(psc, keyHolder));
        return keyHolder.getKey().longValue();
      }

    private void saveIngredientToTaco(Ingredient ingredient, Long tacoId)
      {
        jdbc.update(
            "insert into TACO_INGREDIENTS (taco, ingredient) " + " values (?,?)",tacoId, ingredient.getId()
                    );
      }

  }
