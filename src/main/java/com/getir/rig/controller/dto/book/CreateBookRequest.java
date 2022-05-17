package com.getir.rig.controller.dto.book;

import com.getir.rig.controller.dto.BookDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CreateBookRequest extends BookDto {
}
