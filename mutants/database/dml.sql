CREATE TABLE public.adn_test
(
    is_mutant boolean,
    adn_md5 character varying(32) COLLATE pg_catalog."default"
)

TABLESPACE pg_default;

ALTER TABLE public.adn_test
    OWNER to postgres;
    
CREATE INDEX adn_test_index
    ON public.adn_test USING btree
    (adn_md5 COLLATE pg_catalog."default" ASC NULLS LAST, is_mutant DESC NULLS LAST)
    TABLESPACE pg_default;