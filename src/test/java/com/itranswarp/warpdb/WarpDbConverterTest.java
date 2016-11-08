package com.itranswarp.warpdb;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import com.itranswarp.warpdb.test.Address;
import com.itranswarp.warpdb.test.TodoEntity;

public class WarpDbConverterTest extends WarpDbTestBase {

	@Test
	public void testConvert() throws Exception {
		TodoEntity todo = new TodoEntity();
		todo.id = TodoEntity.nextId();
		todo.name = "Unit Test";
		todo.targetDate = LocalDate.of(2016, 10, 20);
		todo.targetDateTime = LocalDateTime.of(2016, 10, 20, 11, 12, 13);
		todo.address = new Address("Beijing", "No.1 Road", "100101");
		warpdb.save(todo);
		// query:
		TodoEntity bak = warpdb.fetch(TodoEntity.class, todo.id);
		assertNotNull(bak);
		Address addr = bak.address;
		assertNotNull(addr);
		assertEquals("Beijing", addr.city);
		assertEquals("No.1 Road", addr.street);
		assertEquals("100101", addr.zip);
		// update:
		bak.address = new Address("Shanghai", "#1 Street", "200202");
		warpdb.update(bak);
		// query:
		TodoEntity bak2 = warpdb.fetch(TodoEntity.class, todo.id);
		assertNotNull(bak2);
		Address addr2 = bak2.address;
		assertNotNull(addr2);
		assertEquals("Shanghai", addr2.city);
		assertEquals("#1 Street", addr2.street);
		assertEquals("200202", addr2.zip);
	}

	@Test
	public void testConvertNull() throws Exception {
		TodoEntity todo = new TodoEntity();
		todo.id = TodoEntity.nextId();
		todo.name = "Unit Test";
		todo.targetDate = LocalDate.of(2016, 10, 20);
		todo.targetDateTime = LocalDateTime.of(2016, 10, 20, 11, 12, 13);
		todo.address = null;
		warpdb.save(todo);
		// query:
		TodoEntity bak = warpdb.fetch(TodoEntity.class, todo.id);
		assertNotNull(bak);
		assertNull(bak.address);
		// update:
		bak.name = "Changed";
		warpdb.update(bak);
		// query:
		TodoEntity bak2 = warpdb.fetch(TodoEntity.class, todo.id);
		assertNotNull(bak2);
		assertNull(bak2.address);
	}

	@Test
	public void testConvertWhenQuery() throws Exception {
		TodoEntity todo = new TodoEntity();
		todo.id = TodoEntity.nextId();
		todo.name = "Unit Test";
		todo.targetDate = LocalDate.of(2016, 10, 20);
		todo.targetDateTime = LocalDateTime.of(2016, 10, 20, 11, 12, 13);
		todo.address = null;
		warpdb.save(todo);
		// query:
		List<TodoEntity> list = warpdb.from(TodoEntity.class).where("targetDate>?", LocalDate.of(2016, 10, 19)).list();
		assertEquals(1, list.size());
		assertEquals(LocalDate.of(2016, 10, 20), list.get(0).targetDate);
	}
}
