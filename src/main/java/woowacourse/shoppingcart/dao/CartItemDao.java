package woowacourse.shoppingcart.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.exception.InvalidCartItemException;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItem> ROM_MAPPER = (rs, rn) -> {
        Product product = new Product(
            rs.getLong("product_id"), rs.getString("product_name"),
            rs.getInt("product_price"), rs.getString("product_image_url"));
        return new CartItem(rs.getLong("id"), rs.getLong("account_id"),
            product, rs.getInt("quantity"));
    };

    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> findProductIdsByCustomerId(final Long customerId) {
        final String sql = "SELECT product_id FROM cart_item WHERE account_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("product_id"), customerId);
    }

    public List<Long> findIdsByCustomerId(final Long customerId) {
        final String sql = "SELECT id FROM cart_item WHERE account_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("id"), customerId);
    }

    public Long addCartItem(final Long customerId, final Long productId) {
        final String sql = "INSERT INTO cart_item(account_id, product_id) VALUES(?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, productId);
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void deleteCartItem(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";

        final int rowCount = jdbcTemplate.update(sql, id);
        if (rowCount == 0) {
            throw new InvalidCartItemException();
        }
    }

    public Long save(CartItem cartItem) {
        String sql = "insert into cart_item(product_id, account_id, quantity) values (?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement =
                connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setLong(1, cartItem.getProduct().getId());
            preparedStatement.setLong(2, cartItem.getAccountId());
            preparedStatement.setInt(3, cartItem.getQuantity());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean existByAccountIdAndProductId(Long accountId, Long productId) {
        final String query = "SELECT EXISTS(SELECT id FROM cart_item WHERE account_id = ? and product_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, accountId, productId);
    }

    public List<CartItem> findByAccountId(long accountId) {
        String sql = "select c.id as id, c.account_id as account_id, c.quantity as quantity, "
            + "p.id as product_id, p.name as product_name, p.price as product_price, "
            + "p.image_url as product_image_url "
            + "from cart_item as c join product as p on c.product_id = p.id "
            + "where c.account_id = ?";
        return jdbcTemplate.query(sql, ROM_MAPPER, accountId);
    }

    public void deleteCartItem(long accountId, long productId) {
        jdbcTemplate.update("delete from cart_item where account_id = ? and product_id = ?",
            accountId, productId);
    }

    public void updateCartItem(Long accountId, Long productId, int quantity) {
        String sql = "update cart_item set quantity = ? where account_id = ? and product_id = ?";
        jdbcTemplate.update(sql, quantity, accountId, productId);
    }

    public CartItem findByAccountIdAndProductId(Long accountId, Long productId) {
        String sql = "select c.id as id, c.account_id as account_id, c.quantity as quantity, "
            + "p.id as product_id, p.name as product_name, p.price as product_price, "
            + "p.image_url as product_image_url "
            + "from cart_item as c join product as p on c.product_id = p.id "
            + "where c.account_id = ? and p.id = ?";
        return jdbcTemplate.queryForObject(sql, ROM_MAPPER, accountId, productId);
    }
}
